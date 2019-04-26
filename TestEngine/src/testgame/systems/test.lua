function getRequiredComponents()
	return {rectangle = {"position", "size"},
	        point = {"position"}}
end

function isPausable()
	return false
end

timer = 0

function update()
  timer = (timer + 0.07) % 360
  for name, rect in pairs(rectangle) do
    rect.position.x = 500 + 500 * math.sin(timer)
  end
end