function getRequiredComponents()
  return {"position"}
end

function update()
  timer = game.core:getTick()

  for i, entity in ipairs(entities) do
    entity.position.x = 50 + 50 * math.sin(timer*0.1)
  end
end
