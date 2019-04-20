function getRequiredComponents()
  return {"test"}
end


function render(entity, g)
  g:drawRect(entity.test.x, 0, 100, 100)
end